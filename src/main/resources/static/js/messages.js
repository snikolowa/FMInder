document.addEventListener('DOMContentLoaded', async function () {
    const messagesList = document.getElementById('messagesList');
    const userId = sessionStorage.getItem('userId');

    try {
        const response = await fetch(`/chat`);
        const chatData = await response.json();

        for (const message of chatData) {
            console.log(message);
            const userResponse = await fetch(`/users/${message.senderId}`);
            const userData = await userResponse.json();
            console.log(userData);

            const listItem = document.createElement('li');
            listItem.classList.add('message');

            const senderName = userData.firstName + ' ' + userData.lastName;
            const messageContent = message.message;

            const chatAnchor = document.createElement('a');
            chatAnchor.href = '/chat';

            chatAnchor.addEventListener('click', function(event) {
                event.preventDefault();

                sessionStorage.setItem('receiverId', message.senderId);

                window.location.href = '/api/chat';
            });

            const sender = document.createElement('span');
            sender.textContent =  senderName;

            const messageText = document.createElement('span');
            messageText.textContent = messageContent;

            const messageTimestamp = document.createElement('span');
            messageTimestamp.textContent = 'Sent at ' + formatTimestamp(message.timestamp);

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