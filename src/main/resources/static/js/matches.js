document.addEventListener('DOMContentLoaded', async function () {
    const userId= sessionStorage.getItem('userId');
    const acceptButtons = document.querySelectorAll('.accept-button');
    const declineButtons = document.querySelectorAll('.decline-button');
    const sendRequestButtons = document.querySelectorAll('.send-request-button');
    const matchRequestsList = document.querySelector('.requests-list');
    const potentialMatchesList = document.querySelector('.potential-matches-list');

    try {
        const matchRequestsResponse = await fetch(`/users/${userId}/requests`);
        const matchRequestsData = await matchRequestsResponse.json();

        updateMatchesList(matchRequestsData, matchRequestsList, 'Connect');

    } catch (error) {
        console.error('Error fetching match requests:', error);
    }

    try {
        const potentialMatchesResponse = await fetch(`/users/${userId}/potential-matches`);
        const potentialMatchesData = await potentialMatchesResponse.json();

        updateMatchesList(potentialMatchesData, potentialMatchesList, 'Accept', 'Decline');

    } catch (error) {
        console.error('Error fetching potential matches:', error);
    }

    function removeItemFromList(listElement, id) {
        const listItem = listElement.querySelector(`[data-user-id="${id}"]`);

        if (listItem) {
            listItem.remove();
        }
    }

    attachButtonListeners(acceptButtons, 'Accepted');
    attachButtonListeners(declineButtons, 'Denied');
    attachButtonListeners(sendRequestButtons, 'Connect', true);

    function attachButtonListeners(buttons, status, isRequest = false) {
        buttons.forEach(button => {
            button.addEventListener('click', async function () {
                const id = getIdFromButton(button, isRequest);

                await updateRequestStatus(id, status, isRequest);

                removeItemFromList(isRequest ? potentialMatchesList : matchRequestsList, id);
            });
        });
    }

    function getIdFromButton(button, isRequest) {
        const listItem = button.closest('li');

        return listItem.dataset[isRequest ? 'requestId' : 'userId'];
    }

    function updateMatchesList(matchesData, listElement, primaryButtonLabel, secondaryButtonLabel) {
        listElement.innerHTML = '';

        if (!Array.isArray(matchesData) || matchesData.length === 0) {
            const placeholderItem = document.createElement('li');
            placeholderItem.textContent = 'No current matches';
            listElement.appendChild(placeholderItem);
            return;
        }

        matchesData.forEach(match => {
            const listItem = document.createElement('li');

            const profileInfoContainer = document.createElement('div');
            profileInfoContainer.classList.add('profile-info');

            const profilePicture = document.createElement('img');
            profilePicture.src = match.profilePicture || '../assets/placeholder.png';
            profilePicture.alt = 'Profile Picture';

            const usernameAnchor = document.createElement('a');
            usernameAnchor.href = `/api/profile/${match.userId}`;
            usernameAnchor.textContent = match.username;

            profileInfoContainer.appendChild(profilePicture);
            profileInfoContainer.appendChild(usernameAnchor);

            const actionButtonsContainer = document.createElement('div');
            actionButtonsContainer.classList.add('action-buttons');

            const primaryButton = document.createElement('button');
            primaryButton.classList.add(isRequest ? 'send-request-button' : 'accept-button');
            primaryButton.textContent = primaryButtonLabel;

            if (secondaryButtonLabel) {
                const secondaryButton = document.createElement('button');
                secondaryButton.classList.add('decline-button');
                secondaryButton.textContent = secondaryButtonLabel;
                actionButtonsContainer.appendChild(secondaryButton);
            }

            actionButtonsContainer.appendChild(primaryButton);

            listItem.appendChild(profileInfoContainer);
            listItem.appendChild(actionButtonsContainer);

            listElement.appendChild(listItem);
        });
    }

    async function updateRequestStatus(id, status, isRequest) {
        try {
            const endpoint = isRequest ? '/requests' : `/requests/${id}/${status}`;
            const response = await fetch(endpoint, {
                method: isRequest ? 'POST' : 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: isRequest ? JSON.stringify({ receiverUserId: id }) : undefined,
            });

            if (!response.ok) {
                throw new Error('Failed to update request status');
            }

        } catch (error) {
            console.error('Error:', error);
        }
    }
});