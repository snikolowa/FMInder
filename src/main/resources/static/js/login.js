document.getElementById('loginForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const email = document.getElementsByName('email')[0].value;
    const password = document.getElementsByName('password')[0].value;

    const user = {
        email,
        password,
    };

    try {
        const response = await fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user),
        });

        if (response.ok) {
            const userData = await response.json();

            sessionStorage.setItem('userId', userData.id);

            window.location.href = '/api/matches';
        } else {
            console.error('Login failed:', response.statusText);
        }
    } catch (error) {
        console.error('Login failed:', error);
    }
});