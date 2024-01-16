// document.addEventListener('DOMContentLoaded', async function () {
//     document.querySelector('.logout-button').addEventListener('click', async function() {
//         try {
//             const response = await fetch('/logout', {
//                 method: 'POST',
//                 headers: {
//                     'Content-Type': 'application/json'
//                 }
//             });
//
//             if (!response.ok) {
//                 throw new Error(`HTTP error! status: ${response.status}`);
//             }
//
//             window.location.href = '/api/login';
//         } catch (error) {
//             console.error('Logout failed:', error);
//         }
//     });
// })