document.addEventListener('DOMContentLoaded', async function () {
    const messagesList = document.getElementById('messagesList');
    const userId = sessionStorage.getItem('userId');

    const profileLink = document.getElementById('nav-profile');

    if (profileLink) {
        profileLink.addEventListener('click', function() {
            if (sessionStorage.getItem('matchId')) {
                sessionStorage.removeItem('matchId');
            }
        });
    }

    try {
        const response = await fetch(`/chat`);
        const chatData = await response.json();

        for (const message of chatData) {
            // if (message.receiverId === userId) {
            //     continue;
            // }

            const userResponse = await fetch(`/users/${message.senderId}`);
            const userData = await userResponse.json();
            const receiverResponse = await fetch(`/users/${message.receiverId}`);
            const receiverData = await receiverResponse.json();

            const listItem = document.createElement('li');
            listItem.classList.add('message');

            const senderName = (message.senderId === +userId)
                ? receiverData.firstName + ' ' + receiverData.lastName
                : userData.firstName + ' ' + userData.lastName;

            const messageContent = message.message;

            const chatAnchor = document.createElement('a');
            chatAnchor.href = '/chat';

            chatAnchor.addEventListener('click', function(event) {
                event.preventDefault();

                sessionStorage.setItem('receiverId', message.senderId === +userId ? message.receiverId : message.senderId);

                window.location.href = '/api/chat';
            });

            const profilePicture = document.createElement('img');
            profilePicture.src = (message.senderId === +userId)
                ? (receiverData.profilePicture ? `data:image/jpeg;base64,${receiverData.profilePicture}` : '../assets/placeholder.png')
                : (userData.profilePicture ? `data:image/jpeg;base64,${userData.profilePicture}` : '../assets/placeholder.png');
            profilePicture.alt = 'Profile Picture';

            const sender = document.createElement('span');
            sender.textContent =  senderName;

            const messageText = document.createElement('span');
            messageText.textContent = messageContent;

            const messageTimestamp = document.createElement('span');
            messageTimestamp.textContent = 'Sent at ' + formatTimestamp(message.timestamp);

            chatAnchor.appendChild(profilePicture);
            chatAnchor.appendChild(sender);
            chatAnchor.appendChild(messageText);
            chatAnchor.appendChild(messageTimestamp);
            listItem.appendChild(chatAnchor);
            messagesList.appendChild(listItem);
        }
    } catch (error) {
        console.error('Error fetching chat data:', error);
    }

    function formatTimestamp(timestamp) {
        const date = new Date(timestamp);
        const formattedDate = `${padZero(date.getDate())}.${padZero(date.getMonth() + 1)}`; // DD.MM format
        const formattedTime = `${padZero(date.getHours())}:${padZero(date.getMinutes())}`; // HH:MM format
        return `${formattedDate} ${formattedTime}`;
    }

    function padZero(value) {
        return value < 10 ? `0${value}` : value;
    }
});