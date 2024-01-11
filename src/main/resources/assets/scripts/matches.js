document.addEventListener('DOMContentLoaded', async function () {
    // Select elements and initialize variables
    const acceptButtons = document.querySelectorAll('.accept-button');
    const declineButtons = document.querySelectorAll('.decline-button');
    const sendRequestButtons = document.querySelectorAll('.send-request-button');
    const matchRequestsList = document.querySelector('.requests-list');
    const potentialMatchesList = document.querySelector('.potential-matches-list');

    // Fetch match requests
    try {
        const userId = 1; // Replace with the actual user ID
        const matchRequestsResponse = await fetch(`/users/${userId}/requests`);
        const matchRequestsData = await matchRequestsResponse.json();

        // Update the DOM with match requests data
        updateMatchesList(matchRequestsData, matchRequestsList, 'Connect');

    } catch (error) {
        console.error('Error fetching match requests:', error);
    }

    // Fetch potential matches
    try {
        const userId = 1; // Replace with the actual user ID
        const potentialMatchesResponse = await fetch(`/users/${userId}/potential-matches`);
        const potentialMatchesData = await potentialMatchesResponse.json();

        // Update the DOM with potential matches data
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

    // Attach event listeners for button clicks
    attachButtonListeners(acceptButtons, 'Accepted');
    attachButtonListeners(declineButtons, 'Denied');
    attachButtonListeners(sendRequestButtons, 'Connect', true);

    // Function to attach event listeners to buttons
    function attachButtonListeners(buttons, status, isRequest = false) {
        buttons.forEach(button => {
            button.addEventListener('click', async function () {
                const id = getIdFromButton(button, isRequest);
                await updateRequestStatus(id, status);
                removeItemFromList(isRequest ? potentialMatchesList : matchRequestsList, id);
            });
        });
    }

    // Function to get ID from button's parent list item
    function getIdFromButton(button, isRequest) {
        const listItem = button.closest('li');
        return listItem.dataset[isRequest ? 'requestId' : 'userId'];
    }

    // Function to update matches list in the DOM
    function updateMatchesList(matchesData, listElement, primaryButtonLabel, secondaryButtonLabel) {
        // Clear existing content
        listElement.innerHTML = '';
        
        if (matchesData.length === 0) {
            const placeholderItem = document.createElement('li');
            placeholderItem.textContent = 'No current matches';
            listElement.appendChild(placeholderItem);
            return;
        }

        // Iterate through the matchesData and update the DOM
        matchesData.forEach(match => {
            // Create list item
            const listItem = document.createElement('li');

            // Create profile info container
            const profileInfoContainer = document.createElement('div');
            profileInfoContainer.classList.add('profile-info');

            // Create profile picture element
            const profilePicture = document.createElement('img');
            profilePicture.src = '../assets/images/placeholder.png';
            profilePicture.alt = 'Profile Picture';
            
            // Create anchor element for the clickable username
            const usernameAnchor = document.createElement('a');
            usernameAnchor.href = `/profile/${match.userId}`; // Set the correct profile page URL
            usernameAnchor.textContent = match.username; // Replace 'username' with the actual property in matchesData

            // Append profile picture and username to profile info container
            profileInfoContainer.appendChild(profilePicture);
            profileInfoContainer.appendChild(usernameElement);

            // Create action buttons container
            const actionButtonsContainer = document.createElement('div');
            actionButtonsContainer.classList.add('action-buttons');

            // Create primary button (e.g., 'Connect' or 'Accept')
            const primaryButton = document.createElement('button');
            primaryButton.classList.add(isRequest ? 'send-request-button' : 'accept-button');
            primaryButton.textContent = primaryButtonLabel;

            // Create secondary button (e.g., 'Decline')
            if (secondaryButtonLabel) {
                const secondaryButton = document.createElement('button');
                secondaryButton.classList.add('decline-button');
                secondaryButton.textContent = secondaryButtonLabel;

                // Append secondary button to action buttons container
                actionButtonsContainer.appendChild(secondaryButton);
            }

            // Append primary button to action buttons container
            actionButtonsContainer.appendChild(primaryButton);

            // Append profile info and action buttons to the list item
            listItem.appendChild(profileInfoContainer);
            listItem.appendChild(actionButtonsContainer);

            // Append the list item to the list element
            listElement.appendChild(listItem);
        });
    }

    // Function to update the status of a request
    async function updateRequestStatus(id, status) {
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