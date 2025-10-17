document.addEventListener('DOMContentLoaded', () => {
  const attendBtn = document.getElementById('attendButton');
  const cancelBtn = document.getElementById('attendCancelButton');

  // 출석
  if (attendBtn) {
    attendBtn.addEventListener('click', async (e) => {
      e.preventDefault();
      attendBtn.disabled = true;
      try {
        const r = await fetch('/auth/api/attend', { method: 'GET' });
        const txt = await r.text();
        const ok = parseInt(txt, 10) === 1;
        alert(ok ? '출석 완료' : '이미 출석하였습니다.');

        location.reload();
      } catch (err) {
        console.error(err);
        alert('출석 처리 실패');
      } finally {
        attendBtn.disabled = false;
      }
    });
  }

  // 출석 취소
  if (cancelBtn) {
    cancelBtn.addEventListener('click', async (e) => {
      e.preventDefault();
      cancelBtn.disabled = true;
      try {
        const r = await fetch('/auth/api/attendCancel', { method: 'GET' });
        const txt = await r.text();
        const ok = parseInt(txt, 10) === 1;
        alert(ok ? '출석 취소 완료' : '이미 취소하였습니다.');

        location.reload();
      } catch (err) {
        console.error(err);
        alert('취소 처리 실패');
      } finally {
        cancelBtn.disabled = false;
      }
    });
  }
});