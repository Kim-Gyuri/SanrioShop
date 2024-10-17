$(document).ready(function () {
  const accessToken = localStorage.getItem('accessToken') ? localStorage.getItem('accessToken').trim() : '';

  // 알림 개수를 업데이트하는 함수
  function updateNotificationCount() {
    $.ajax({
      url: '/api/message',
      type: 'GET',
      headers: {
        'Authorization': accessToken
      },
      success: function (notifications) {
        const count = notifications.length;
        $('#notification-count').text(count); // 알림 개수를 업데이트
      },
      error: function (err) {
        console.error('Failed to load notifications:', err);
      }
    });
  }

  // 알림 버튼 클릭 이벤트 핸들러
  $('#notification-button').on('click', function (event) {
    event.preventDefault();

    $.ajax({
      url: '/api/message',
      type: 'GET',
      headers: {
        'Authorization': accessToken
      },
      success: function (notifications) {
        $('#notification-list').empty(); // 기존 알림 내용 초기화

        if (notifications.length === 0) {
          $('#notification-list').append('<p>새로운 알림이 없습니다.</p>');
        } else {
          notifications.forEach(function (notification) {
            $('#notification-list').append(`
              <div class="notification-item d-flex justify-content-between align-items-center border-bottom py-2">
                <p class="mb-0 text-truncate w-75">${notification.message}</p>
                <button class="btn btn-sm btn-danger delete-notification-btn" data-id="${notification.id}">삭제</button>
              </div>
            `);
          });
        }

        $('#notificationModal').modal('show'); // 모달 열기
      },
      error: function (err) {
        console.error('Failed to load notifications:', err);
      }
    });
  });

  // 삭제 버튼 클릭 이벤트 핸들러
  $('#notification-list').on('click', '.delete-notification-btn', function () {
    const messageId = $(this).data('id');

    if (confirm('정말로 이 알림을 삭제하시겠습니까?')) {
      $.ajax({
        url: `/api/message/${messageId}`,
        type: 'DELETE',
        headers: {
          'Authorization': accessToken
        },
        success: function () {
          alert('알림이 삭제되었습니다.');
          updateNotificationCount(); // 알림 개수 업데이트
          $('#notification-button').trigger('click'); // 목록 새로고침
        },
        error: function (err) {
          console.error('Failed to delete notification:', err);
          alert('알림 삭제에 실패했습니다.');
        }
      });
    }
  });

  // 초기 로딩 시 알림 개수 업데이트
  updateNotificationCount();
});