document.getElementById("showQueue1").addEventListener("click", function(){
    const queue = window.queueData || [];

    let html = '<ol>';

    if (Array.isArray(queue) && queue.length > 0) {
        for (const match of queue) {
            if (Array.isArray(match) && match.length === 2
                && Array.isArray(match[0]) && Array.isArray(match[1])) {

                const teamA = match[0].join(' & ');
                const teamB = match[1].join(' & ');
                html += `<li><${teamA}> vs <${teamB}></li>`;
            }
        }
    } else {
        html += '<li>대기열이 없습니다.</li>';
    }

    html += '</ol>';

    document.getElementById('queueContent1').innerHTML = html;
    const modal = new bootstrap.Modal(document.getElementById('queueModal1'));
    modal.show();
});

document.getElementById("showQueue2").addEventListener("click", function(){
    const queue = [
        [['봉미선', '짱아'], ['철수', '훈이']],
        [['맹구', '유리'], ['짱구', '기철']]
    ];

    let html = '<ul>';

    if (Array.isArray(queue) && queue.length > 0) {
        for (const match of queue) {
            // match 배열 구조가 올바른지도 체크하면 더 안전
            if (Array.isArray(match) && match.length === 2
                && Array.isArray(match[0]) && Array.isArray(match[1])) {

                const teamA = match[0].join(' & ');
                const teamB = match[1].join(' & ');
                html += `<li><${teamA}> vs <${teamB}></li>`;
            }
        }
    } else {
        html += '<li>대기열이 없습니다.</li>';
    }

    html += '</ul>';

    document.getElementById('queueContent2').innerHTML = html;
    const modal = new bootstrap.Modal(document.getElementById('queueModal2'));
    modal.show();
});

document.getElementById("showQueue3").addEventListener("click", function(){
    const queue = [

    ];

    let html = '<ul>';

    if (Array.isArray(queue) && queue.length > 0) {
        for (const match of queue) {
            // match 배열 구조가 올바른지도 체크하면 더 안전
            if (Array.isArray(match) && match.length === 2
                && Array.isArray(match[0]) && Array.isArray(match[1])) {

                const teamA = match[0].join(' & ');
                const teamB = match[1].join(' & ');
                html += `<li><${teamA}> vs <${teamB}></li>`;
            }
        }
    } else {
        html += '<li>대기열이 없습니다.</li>';
    }

    html += '</ul>';

    document.getElementById('queueContent3').innerHTML = html;
    const modal = new bootstrap.Modal(document.getElementById('queueModal3'));
    modal.show();
});