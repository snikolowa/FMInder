document.addEventListener('DOMContentLoaded', async function () {
    const userId = sessionStorage.getItem('userId');
    const matchRequestsList = document.querySelector('.requests-list');
    const potentialMatchesList = document.querySelector('.potential-matches-list');
    let isRequest = true;

    try {
        const matchRequestsData = await fetchMatchRequests(userId);
        updateMatchesList(matchRequestsData, matchRequestsList, 'Accept', 'Decline', isRequest = false);
    } catch (error) {
        console.error('Error fetching match requests:', error);
    }

    try {
        const potentialMatchesData = await fetchPotentialMatches();
        console.log(potentialMatchesData);
        updateMatchesList(potentialMatchesData, potentialMatchesList, 'Connect', '', isRequest = true);
    } catch (error) {
        console.error('Error fetching potential matches:', error);
    }

    async function fetchMatchRequests(userId) {
        try {
            const matchRequestsResponse = await fetch(`/users/${userId}/requests`);
            const matchRequests = await matchRequestsResponse.json();
            console.log('Match Requests: ', matchRequests);

            return matchRequests.map(entry => {
                return {
                    ...entry.user,
                    requestId: entry.request.id
                };
            });
        } catch (error) {
            throw new Error('Error fetching match requests:', error);
        }
    }

    async function fetchPotentialMatches() {
        try {
            const potentialMatchesResponse = await fetch(`/matches`);
            return await potentialMatchesResponse.json();
        } catch (error) {
            throw new Error('Error fetching potential matches:', error);
        }
    }

    function removeItemFromList(listElement, id) {
        const listItem = listElement.querySelector(`[data-user-id="${id}"]`);

        if (listItem) {
            listItem.remove();
        }
    }

    function getIdFromButton(button, isRequest) {
        const listItem = button.closest('li');
        return isRequest ? listItem.dataset.requestId : listItem.dataset.userId;
    }

    function updateMatchesList(matchesData, listElement, primaryButtonLabel, secondaryButtonLabel, isRequest) {
        listElement.innerHTML = '';

        if (!Array.isArray(matchesData) || matchesData.length === 0) {
            const placeholderItem = document.createElement('li');
            placeholderItem.textContent = 'No current matches';
            listElement.appendChild(placeholderItem);
            return;
        }

        matchesData.forEach(match => {
            console.log('Match: ', match);
            const listItem = document.createElement('li');
            listItem.dataset.requestId = match.requestId;

            const profileInfoContainer = document.createElement('div');
            profileInfoContainer.classList.add('profile-info');

            const profilePicture = document.createElement('img');
            profilePicture.src = match.profilePicture || '../assets/placeholder.png';
            profilePicture.alt = 'Profile Picture';

            const usernameAnchor = document.createElement('a');
            usernameAnchor.href = `/api/profile`;
            usernameAnchor.textContent = match.firstName + ' ' + match.lastName;

            usernameAnchor.addEventListener('click', function (e) {
                e.preventDefault();

                sessionStorage.setItem('matchId', match.userId);

                window.location.href = this.href;
            });

            profileInfoContainer.appendChild(profilePicture);
            profileInfoContainer.appendChild(usernameAnchor);

            const actionButtonsContainer = document.createElement('div');
            actionButtonsContainer.classList.add('action-buttons');

            const primaryButton = document.createElement('button');
            primaryButton.classList.add(isRequest ? 'send-request-button' : 'accept-button');
            primaryButton.textContent = primaryButtonLabel;
            actionButtonsContainer.appendChild(primaryButton);

            if (secondaryButtonLabel) {
                const secondaryButton = document.createElement('button');
                secondaryButton.classList.add('decline-button');
                secondaryButton.textContent = secondaryButtonLabel;
                actionButtonsContainer.appendChild(secondaryButton);
            }

            listItem.appendChild(profileInfoContainer);
            listItem.appendChild(actionButtonsContainer);

            listElement.appendChild(listItem);

            actionButtonsContainer.addEventListener('click', async function (event) {
                const id = getIdFromButton(event.target, isRequest);

                if (event.target.classList.contains('accept-button')) {
                    await updateRequestStatus(listItem.dataset.requestId, primaryButtonLabel, isRequest = false);
                } else if (event.target.classList.contains('decline-button')) {
                    await updateRequestStatus(listItem.dataset.requestId, secondaryButtonLabel, isRequest = false);
                } else if (event.target.classList.contains('send-request-button')) {
                    await updateRequestStatus(match.id, 'Connect', isRequest = true);
                }

                removeItemFromList(isRequest ? potentialMatchesList : matchRequestsList, id);
            });
        });
    }

    async function updateRequestStatus(id, status, isRequest) {
        try {
            let endpoint;
            if (isRequest) {
                endpoint = '/requests';
            } else {
                endpoint = status === 'Accept' ? `/requests/${id}/Accepted` : `/requests/${id}/Denied`;
            }

            const response = await fetch(endpoint, {
                method: !isRequest ? 'PATCH' : 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: !isRequest ? JSON.stringify({requestId: id,}) : JSON.stringify({receiverUserId: id,}),
            });

            console.log('API Response:', response);

            if (!response.ok) {
                throw new Error('Failed to update request status');
            }

            if (isRequest) {
                const potentialMatchesData = await fetchPotentialMatches();
                updateMatchesList(potentialMatchesData, potentialMatchesList, 'Connect', '', isRequest = true);
            } else {
                const matchRequestsData = await fetchMatchRequests(userId);
                updateMatchesList(matchRequestsData, matchRequestsList, 'Accept', 'Decline', isRequest = false);
            }

            removeItemFromList(isRequest ? potentialMatchesList : matchRequestsList, id);

        } catch (error) {
            console.error('Error:', error);
        }
    }
});