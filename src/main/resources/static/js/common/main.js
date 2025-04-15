document.getElementById('attendButton').addEventListener('click', function(event) {
    event.preventDefault();  // 페이지 이동 막기

    fetch('/auth/attend', {
        method: 'GET'
    })
    .then(response => response.text())  // 숫자니까 text로 받음
    .then(data => {
        if (parseInt(data) === 1) {
            alert('출석 완료');
        } else {
            alert('출석 실패');
        }
    })
    .catch(error => console.error('Error:', error));
});