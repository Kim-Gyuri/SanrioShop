
function logout() {
    // 로그아웃 요청 보내기
    fetch('/api/user/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            // 로그아웃 성공 시 로그인 페이지로 이동
            localStorage.removeItem('accessToken');
            window.location.href = '/view/login';
        } else {
            // 로그아웃 실패 시 처리
            alert('로그아웃에 실패했습니다. 다시 시도해 주세요.');
        }
    })
    .catch(error => {
      console.error('오류 발생:', error);
    });
}
