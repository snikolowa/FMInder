document.getElementById('discard-button').addEventListener('click', function () {
    window.location.href = 'profile.html';
});

document.addEventListener('DOMContentLoaded', async function () {
    const discardButton = document.getElementById('discard-button');

    if (discardButton) {
        discardButton.addEventListener('click', function () {
            window.location.href = 'profile.html';
        });
    }
    
    const userId = 1; // Replace with the actual user ID
    const userData = await getUserData(userId);

    document.getElementById('edit-info-form').elements['email'].value = userData.email;
    document.getElementById('edit-info-form').elements['first-name'].value = userData.firstName;
    document.getElementById('edit-info-form').elements['last-name'].value = userData.lastName;
    document.getElementById('edit-info-form').elements['gender'].value = userData.gender;
    document.getElementById('edit-info-form').elements['class-year'].value = userData.classYear;
    document.getElementById('edit-info-form').elements['major'].value = userData.major;
    document.getElementById('edit-info-form').elements['interests'].value = userData.interests;

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
        const response = await fetch(`/users/profile/${userId}`);

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
        const userId = 1; // Replace with the actual user ID
        const response = await fetch(`/users/profile/${userId}`, {
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