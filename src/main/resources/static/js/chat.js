document.addEventListener("DOMContentLoaded", async function () {
    const userId = sessionStorage.getItem("userId");
    const receiverId = sessionStorage.getItem("receiverId");

    const profileLink = document.getElementById('nav-profile');

    if (profileLink) {
        profileLink.addEventListener('click', function() {
            if (sessionStorage.getItem('matchId')) {
                sessionStorage.removeItem('matchId');
            }
        });
    }

    await fetchChat();

    async function fetchChat() {
        try {
            const response = await fetch(`/chat/${receiverId}`);
            const messages = await response.json();

            const otherUserId =  userId === receiverId ? messages[0].senderId : receiverId;
            const otherUserResponse = await fetch(`/users/${otherUserId}`);
            const otherUserData = await otherUserResponse.json();

            const chatHeader = document.querySelector(".chat-container h2");

            if (chatHeader) {
                chatHeader.textContent = `Chat with ${otherUserData.firstName} ${otherUserData.lastName}`;
            }

            displayMessages(messages);
        } catch (error) {
            console.error("Error fetching chat:", error);
        }
    }

    async function displayMessages(messages) {
        const messagesList = document.getElementById("messagesList");

        messagesList.innerHTML = "";

        for (const message of messages) {
            try {
                const userResponse = await fetch(`/users/${message.senderId}`);
                const userData = await userResponse.json();

                const li = document.createElement("li");
                li.classList.add('message');

                const profilePicture = document.createElement('img');
                profilePicture.src = userData.profilePicture ? `data:image/jpeg;base64,${userData.profilePicture}` : '../assets/placeholder.png';
                profilePicture.alt = 'Profile Picture';

                const sender = document.createElement('span');
                sender.textContent =  userData.firstName + ' ' + userData.lastName;

                const messageText = document.createElement('span');
                messageText.textContent = message.message;

                const messageTimestamp = document.createElement('span');
                messageTimestamp.textContent = 'Sent at ' + formatTimestamp(message.timestamp);

                li.appendChild(profilePicture);
                li.appendChild(sender);
                li.appendChild(messageText);
                li.appendChild(messageTimestamp);
                messagesList.appendChild(li);
            } catch (error) {
                console.error("Error fetching user data:", error);
            }
        }
    }

    async function sendMessage() {
        const messageInput = document.getElementById("messageInput");
        const messageContent = messageInput.value.trim();

        if (messageContent) {
            const message = {
                message: messageContent
            };

            try {
                const response = await fetch(`/chat/${receiverId}`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(message)
                });

                if (!response.ok) {
                    throw new Error('Failed to update request status');
                }

                messageInput.value = "";

                fetchChat();
            } catch (error) {
                console.error("Error sending message:", error);
            }
        }
    }

    document.getElementById("sendButton").addEventListener("click", sendMessage);

    function formatTimestamp(timestamp) {
        const date = new Date(timestamp);
        const formattedDate = `${padZero(date.getDate())}.${padZero(date.getMonth() + 1)}`;
        const formattedTime = `${padZero(date.getHours())}:${padZero(date.getMinutes())}`;
        return `${formattedDate} ${formattedTime}`;
    }

    function padZero(value) {
        return value < 10 ? `0${value}` : value;
    }
});