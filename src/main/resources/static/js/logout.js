document.querySelector('.logout-button').addEventListener('click', async function () {
    try {
        const response = await fetch('/logout', {
            method: 'POST',
        });

        window.location.href = '/api/login';
    } catch (error) {
        console.error('Logout failed:', error);
    }
});