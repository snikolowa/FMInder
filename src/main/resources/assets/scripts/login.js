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

        const data = await response.json();
        console.log('User logged in:', data);
    } catch (error) {
        console.error('Login failed:', error);
    }
});