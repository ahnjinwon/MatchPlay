document.addEventListener('DOMContentLoaded', function() {
    const attendButton = document.getElementById('attendButton');
    const attendCancelButton = document.getElementById('attendCancelButton');

    // 'attendButton'이 있을 때만 이벤트 리스너 추가
    if (attendButton) {
        attendButton.addEventListener('click', function(event) {
            event.preventDefault();  // 페이지 이동 막기

            fetch('/auth/api/attend', {
                method: 'GET'
            })
            .then(response => response.text())  // 숫자니까 text로 받음
            .then(data => {
                if (parseInt(data) === 1) {
                    alert('출석 완료');
                    location.reload();
                } else {
                    alert('이미 출석하였습니다.');
                    location.reload();
                }
            })
            .catch(error => console.error('Error:', error));
        });
    }

    // 'attendCancelButton'이 있을 때만 이벤트 리스너 추가
    if (attendCancelButton) {
        attendCancelButton.addEventListener('click', function(event) {
            event.preventDefault();  // 페이지 이동 막기

            fetch('/auth/api/attendCancel', {
                method: 'GET'
            })
            .then(response => response.text())  // 숫자니까 text로 받음
            .then(data => {
                if (parseInt(data) === 1) {
                    alert('출석 취소 완료');
                    location.reload();
                } else {
                    alert('이미 취소하였습니다.');
                    location.reload();
                }
            })
            .catch(error => console.error('Error:', error));
        });
    }
});