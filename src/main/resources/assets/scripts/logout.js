document.getElementById('logout-button').addEventListener('click', async function () {
    try {
        const response = await fetch('/logout', {
            method: 'POST',
        });

        const data = await response.text();
        console.log('User logged out:', data);
    } catch (error) {
        console.error('Logout failed:', error);
    }
});