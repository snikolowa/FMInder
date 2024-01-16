document.addEventListener('DOMContentLoaded', async function () {
    const messagesList = document.getElementById('messagesList');
    const receiverId = sessionStorage.getItem('userId');

    try {
        const response = await fetch(`/chat/${receiverId}`);
        const chatData = await response.json();

        updateChatList(chatData);
    } catch (error) {
        console.error('Error fetching chat data:', error);
    }

    function updateChatList(chatData) {
        messagesList.innerHTML = '';

        if (!Array.isArray(chatData) || chatData.length === 0) {
            const placeholderItem = document.createElement('li');
            placeholderItem.textContent = 'No messages available';
            messagesList.appendChild(placeholderItem);
            return;
        }

        chatData.forEach(message => {
            const listItem = document.createElement('li');
            listItem.classList.add('message');

            const senderName = message.senderName;
            const messageContent = message.messageContent;

            listItem.textContent = `${senderName}: ${messageContent}`;
            messagesList.appendChild(listItem);
        });
    }
});