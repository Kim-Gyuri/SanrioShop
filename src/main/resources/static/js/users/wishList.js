// 튤바 메뉴 네임을 확인해주는
document.addEventListener('DOMContentLoaded', function () {

   var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
   var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
   });
});

// 로고 클릭 > 홈 이동
$('#logo-link').on('click', function(event) {
          event.preventDefault();
          currentSearchType = '';
          currentKeyword = '';
          const searchUrl = '/view/home';
          window.location.href = searchUrl;
});

// api 요청
  $(document).ready(function() {
      let currentPage = 1;
      let totalPages = 1;

      let accessToken = localStorage.getItem('accessToken');
      accessToken = accessToken ? accessToken.trim() : '';

      function loadItems(page) {

          $.ajax({
              url: `/api/wish`,
              type: 'GET',
              headers: {
                  'Authorization': accessToken
              },
              data: {
                  page: page - 1,
                  size: 5
              },
              success: function(response) {
                  const items = response._embedded && response._embedded.wishlistItemDtoList ? response._embedded.wishlistItemDtoList : [];
                  totalPages = response.page ? response.page.totalPages : 0;


                  $('#wish-items').empty();

                  if (items.length === 0) {
                    $('#wish-items').append(`
                        <tr>
                            <td colspan="6">
                                <div class="empty-wrapper">
                                    <img src="/images/empty.png" class="item-empty">
                                </div>
                            </td>
                        </tr>
                    `);
                  } else {
                    items.forEach(function(item) {
                        $('#wish-items').append(`
                            <tr class="wish__list__detail">
                                <td><img class="wish-item-img" src="${item.thumbnail}" alt="${item.nameKor}" data-id="${item.id}"></td>
                                <td>
                                    <p>${item.nameKor}</p>
                                    <span class="price">${item.price}원</span>
                                </td>
                                <td></td>
                                <td>
                                    <button class="wish_push_delete" data-item-id="${item.id}" data-wish-item-id="${item.wishItemId}">삭제하기</button>
                                </td>
                            </tr>
                        `);
                    });
                  }

                  currentPage = page;
                  updatePagination();
              },
              error: function(err) {
                  console.error('Failed to load items:', err);
              }
          });
      }

      function updatePagination() {
          $('#page-numbers').empty();

          for (let i = 1; i <= totalPages; i++) {
              const pageButton = $(`<button class="btn ${i === currentPage ? 'btn-selected' : 'btn-outline-secondary'}">${i}</button>`);

              if (i === currentPage) {
                  pageButton.addClass('current');
              }

              pageButton.on('click', function() {
                  loadItems(i);
              });

              $('#page-numbers').append(pageButton);
          }
      }

      // 선택한 상품 페이지로 이동
      $(document).on('click', '.wish-item-img', function(event) {
                  event.preventDefault(); // 링크의 기본 동작 방지

                  const itemId = $(this).data('id');
                  if (itemId) {
                      // item.id를 로컬 스토리지에 저장
                      localStorage.setItem('selectedItemId', itemId);
                      // 페이지 이동
                      window.location.href = '/view/shop/item';
                  }
      });

      // 삭제 버튼 클릭 이벤트
      $('#wish-items').on('click', '.wish_push_delete', function() {
        const itemId = $(this).data('item-id'); // 클릭한 아이템의 ID 가져오기
        const wishItemId = $(this).data('wish-item-id'); // 데이터에서 wishItemId 가져오기

        if (confirm('정말로 이 상품을 삭제하시겠습니까?')) {
            $.ajax({
                url: `/api/wish/wishItem`,
                type: 'DELETE',
                headers: {
                    'Authorization': accessToken,
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify({
                   id: itemId,
                   wishItemId: wishItemId
                }),
                success: function(response) {
                    alert('아이템이 삭제되었습니다.');
                    loadItems(currentPage); // 삭제 후 아이템 목록 새로고침
                },
                error: function(err) {
                    console.error('Failed to delete item:', err);
                    alert('주문요청을 받은 상품은 삭제할 수 없습니다.');
                }
            });
        }
      });


      loadItems(currentPage);

      $('#page-numbers').on('click', '.page-btn', function() {
          let page = $(this).data('page');
          loadItems(page);
      });

  });


