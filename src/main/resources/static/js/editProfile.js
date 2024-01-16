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

    editInfoForm.elements['profile-picture'].value = userData.profilePicture;
    editInfoForm.elements['email'].value = userData.email;
    editInfoForm.elements['first-name'].value = userData.firstName;
    editInfoForm.elements['last-name'].value = userData.lastName;
    editInfoForm.elements['gender'].value = userData.gender;
    editInfoForm.elements['class-year'].value = userData.graduateYear;
    editInfoForm.elements['major'].value = userData.major.replace(/_/g, " ").replace(/\b\w/g, l => l.toUpperCase());
    editInfoForm.elements['interests'].value = userData.interests;

    editInfoForm.addEventListener('submit', async function (e) {
        e.preventDefault();

        const updatedUserData = {
            profilePicture: editInfoForm.elements['profile-picture'].value,
            email: editInfoForm.elements['email'].value,
            password: editInfoForm.elements['password'].value,
            repeatPassword: editInfoForm.elements['repeat-password'].value,
            firstName: editInfoForm.elements['first-name'].value,
            lastName: editInfoForm.elements['last-name'].value,
            gender: editInfoForm.elements['gender'].value,
            graduateYear: editInfoForm.elements['class-year'].value,
            major: editInfoForm.elements['major'].value,
            interests: editInfoForm.elements['interests'].value,
        };

        if (editInfoForm.elements['profile-picture'].files.length > 0) {
            const profilePictureBlob = editInfoForm.elements['profile-picture'].files[0];
            await uploadProfilePicture(profilePictureBlob);
        }

        updateUserProfile(updatedUserData);
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

    async function updateUserProfile(updatedUserData) {
        try {
            const response = await fetch(`/users/profile`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatedUserData),
            });

            if (!response.ok) {
                throw new Error('Failed to update user profile');
            }

            console.log('User profile updated successfully');
        } catch (error) {
            console.error('Error:', error);
        }
    }

    async function uploadProfilePicture(blob) {
        try {
            const formData = new FormData();
            formData.append('profilePicture', blob);

            const response = await fetch('/users/profile/upload-picture', {
                method: 'POST',
                body: formData,
            });

            if (!response.ok) {
                throw new Error('Failed to upload profile picture');
            }

            console.log('Profile picture uploaded successfully');
        } catch (error) {
            console.error('Error:', error);
        }
    }
});