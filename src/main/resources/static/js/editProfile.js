document.getElementById('discard-button').addEventListener('click', function () {
    window.location.href = '/api/profile';
});

document.addEventListener('DOMContentLoaded', async function () {
    const discardButton = document.getElementById('discard-button');

    if (discardButton) {
        discardButton.addEventListener('click', function () {
            window.location.href = '/api/profile';
        });
    }
    
    const userId = sessionStorage.getItem('userId');
    const userData = await getUserData(userId);
    console.log(userData);
    let editInfoForm = document.getElementById('edit-info-form');

    editInfoForm.elements['email'].value = userData.email;
    editInfoForm.elements['first-name'].value = userData.firstName;
    editInfoForm.elements['last-name'].value = userData.lastName;
    editInfoForm.elements['gender'].value = userData.gender;
    editInfoForm.elements['class-year'].value = userData.graduateYear;
    editInfoForm.elements['major'].value = userData.major.replace(/_/g, " ").replace(/\b\w/g, l => l.toUpperCase());
    editInfoForm.elements['interests'].value = userData.interests;

    editInfoForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const updatedUserData = {
            email: editInfoForm.elements['email'].value,
            password: editInfoForm.elements['password'].value,
            repeatPassword: editInfoForm.elements['repeat-password'].value,
            firstName: editInfoForm.elements['first-name'].value,
            lastName: editInfoForm.elements['last-name'].value,
            gender: editInfoForm.elements['gender'].value,
            classYear: editInfoForm.elements['class-year'].value,
            major: editInfoForm.elements['major'].value,
            interests: editInfoForm.elements['interests'].value,
        };

        saveChanges(updatedUserData);
    });
});

async function getUserData(userId) {
    try {
        const response = await fetch(`/users/profile`);

        if (!response.ok) {
            throw new Error('Failed to fetch user data');
        }
        
        const userData = await response.json();
        return userData;
    } catch (error) {
        console.error('Error:', error);
    }
}

async function saveChanges(updatedUserData) {
    try {
        const response = await fetch(`/users/profile`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedUserData),
        });

        if (!response.ok) {
            throw new Error('Failed to save changes');
        }

        console.log('Changes saved successfully');

    } catch (error) {
        console.error('Error:', error);
    }
}