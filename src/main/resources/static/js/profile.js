document.addEventListener('DOMContentLoaded', async function () {
    const userId = sessionStorage.getItem('userId');
    const profileContainer = document.getElementById('container');
    const profileInfo = document.getElementById('user-info');
    const buttonContainer = document.querySelector('.profile-user-actions');

    const pathSegments = window.location.pathname.split('/');
    const profileUserId = pathSegments[pathSegments.indexOf('users') + 1];

    try {
        const userData = await fetchUserData(userId);
        updateProfileInfo(userData);

        const isCurrentUserProfile = (userId === profileUserId);

        if (isCurrentUserProfile) {
            createButton('Message', 'message-button', messageButtonHandler);
            createButton('Unmatch', 'unmatch-button', unmatchButtonHandler);
        } else {
            createButton('Edit info', 'edit-info-button', editInfoButtonHandler);
            createButton('Logout', 'logout-button');

            try {
                const matchRequestsData = await fetchMatchData(userId);
                createMatchContainer(matchRequestsData);
            } catch (error) {
                console.error('Error fetching matches:', error);
            }
        }
    } catch (error) {
        console.error('Error fetching user profile:', error);
    }

    function createMatchContainer(matchRequestsData) {
        const matchContainer = document.createElement('div');
        matchContainer.classList.add('match-container');

        const matchesTitle = document.createElement('h2');
        matchesTitle.textContent = 'My Matches';

        const matchesList = document.createElement('ul');
        matchesList.classList.add('matches-list');

        updateMatchesList(matchRequestsData, matchesList);

        matchContainer.appendChild(matchesTitle);
        matchContainer.appendChild(matchesList);
        profileContainer.appendChild(matchContainer);
    }

    function updateMatchesList(matchesData, listElement) {
        listElement.innerHTML = '';

        if (!Array.isArray(matchesData) || matchesData.length === 0) {
            const placeholderItem = document.createElement('li');
            placeholderItem.textContent = 'No current matches';
            listElement.appendChild(placeholderItem);
            return;
        }

        matchesData.forEach(match => {
            console.log(match);
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

            listItem.appendChild(profileInfoContainer);
            listElement.appendChild(listItem);
        });
    }

    async function fetchUserData(userId) {
        const response = await fetch(`/users/${userId}`);
        return await response.json();
    }

    async function fetchMatchData(userId) {
        const response = await fetch(`/users/${userId}/matches`);
        return await response.json();
    }

    function updateProfileInfo(user) {
        console.log(user);
        profileInfo.innerHTML = ''; // Clear existing content

        const profilePicture = document.createElement('img');
        profilePicture.src = user.profilePicture || '../assets/placeholder.png';
        profilePicture.alt = 'Profile Picture';

        const nameElement = document.createElement('h3');
        nameElement.textContent = user.firstName + ' ' + user.lastName;

        const genderElement = document.createElement('p');
        genderElement.textContent = user.gender;

        const classElement = document.createElement('p');
        classElement.textContent = `Class of ${user.graduateYear}`;

        const majorElement = document.createElement('p');
        majorElement.textContent = `Major in ${user.major}`;

        const interestsElement = document.createElement('p');
        interestsElement.textContent = user.interests ? `Interests: ${user.interests}` : 'No current interests';

        profileInfo.appendChild(profilePicture);
        profileInfo.appendChild(nameElement);
        profileInfo.appendChild(genderElement);
        profileInfo.appendChild(classElement);
        profileInfo.appendChild(majorElement);
        profileInfo.appendChild(interestsElement);
    }

    function createButton(label, className, clickHandler) {
        const button = document.createElement('button');
        button.classList.add(className);
        button.textContent = label;
        button.addEventListener('click', window[clickHandler]); // Attach the click handler
        buttonContainer.appendChild(button);
    }

    function editInfoButtonHandler() {
        window.location.href = '/api/editProfile';
    }

    function messageButtonHandler() {
        window.location.href = '/api/matches';
    }

    function unmatchButtonHandler() {
        window.location.href = '/api/matches';
    }
});