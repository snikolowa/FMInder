document.getElementById('registrationForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const email = document.getElementsByName('email')[0].value;
    const password = document.getElementsByName('password')[0].value;
    const repeatPassword = document.getElementsByName('repeat-password')[0].value;
    const firstName = document.getElementsByName('first-name')[0].value;
    const lastName = document.getElementsByName('last-name')[0].value;
    const gender = document.getElementsByName('gender')[0].value;
    const classYear = document.getElementsByName('class-year')[0].value;
    const major = document.getElementsByName('major')[0].value;

    if (password !== repeatPassword) {
        console.error('Passwords do not match');
        alert('Passwords do not match');
        return;
    }

    const user = {
        email,
        password,
        firstName,
        lastName,
        gender,
        classYear,
        major
    };

    try {
        const response = await fetch('/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user),
        });

        if (!response.ok) {
            throw new Error(`Registration failed with status ${response.status}`);
        }

        window.location.href = '/api/login';
    } catch (error) {
        console.error('Registration failed', error);
    }
});