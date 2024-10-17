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
              url: `/api/orders`,
              type: 'GET',
              headers: {
                  'Authorization': accessToken
              },
              data: {
                  page: page - 1,
                  size: 5
              },
              success: function(response) {
                  const items = response._embedded && response._embedded.customerOrderDtoList ? response._embedded.customerOrderDtoList : [];
                  totalPages = response.page ? response.page.totalPages : 0;

                  $('#order-items').empty();

                  if (items.length === 0) {
                    $('#order-items').append(`
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
                        $('#order-items').append(`
                            <tr class="order__list__detail">
                                <td><img class="order-item-img" src="${item.thumbnail}" alt="${item.nameKor}" data-id="${item.id}"></td>
                                <td>
                                    <p>${item.nameKor}</p>
                                    <span class="price">${item.price}원</span>
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
      $(document).on('click', '.order-item-img', function(event) {
                  event.preventDefault(); // 링크의 기본 동작 방지

                  const itemId = $(this).data('id');
                  if (itemId) {
                      // item.id를 로컬 스토리지에 저장
                      localStorage.setItem('selectedItemId', itemId);
                      // 페이지 이동
                      window.location.href = '/view/shop/item';
                  }
      });

      loadItems(currentPage);

      $('#page-numbers').on('click', '.page-btn', function() {
          let page = $(this).data('page');
          loadItems(page);
      });

  });


