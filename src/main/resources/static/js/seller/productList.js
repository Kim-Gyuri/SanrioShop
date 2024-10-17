  document.addEventListener('DOMContentLoaded', function () {
       // Initialize all tooltips
      var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
      var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
          return new bootstrap.Tooltip(tooltipTriggerEl);
      });
  });


  $(document).ready(function() {
      let currentPage = 1;
      let totalPages = 1;

      let accessToken = localStorage.getItem('accessToken');
      accessToken = accessToken ? accessToken.trim() : '';

      function loadItems(page) {
          console.log('Loading items for page:', page);

          $.ajax({
              url: `/api/sellers/sales`,
              type: 'GET',
              headers: {
                  'Authorization': accessToken
              },
              data: {
                  page: page - 1,
                  size: 5
              },
              success: function(response) {
                  // API 응답의 전체 내용을 콘솔에 출력하여 확인합니다.
                  console.log(response);

                  // 안전하게 응답 데이터에 접근합니다.
                  const items = response._embedded && response._embedded.sellerItemDtoList ? response._embedded.sellerItemDtoList : [];
                  totalPages = response.page ? response.page.totalPages : 0;

                  // 데이터가 비어 있는 경우의 처리
                  if (items.length === 0) {
                      $('#sale-items').empty(); // 이전 항목을 먼저 비웁니다.
                      $('#sale-items').append(`
                        <tr>
                            <td colspan="6">
                                <div class="empty-wrapper">
                                    <img src="/images/empty.png" class="item-empty">
                                </div>
                            </td>
                        </tr>
                      `);
                  } else {
                      $('#sale-items').empty(); // 이전 항목을 먼저 비웁니다.

                      items.forEach(function(item) {
                          $('#sale-items').append(`
                              <tr class="sale__list__detail">
                                  <td>${item.id}</td>
                                  <td><img src="${item.thumbnail}" alt="${item.nameKor}" ></td>
                                  <td>
                                      <p>${item.nameKor}</p>
                                      <span class="price">${item.price}원</span>
                                  </td>
                                  <td class="sale__list__option">
                                      <p>찜 등록 ${item.likeCount}개를 받았어요</p>
                                      <button class="sale__list__optionbtn" data-toggle="modal" data-target="#orderDetailModal" data-item-id="${item.id}">주문요청 보러가기</button>
                                  </td>
                                  <td>
                                      <button class="sale_push_edit" data-item-id="${item.id}">수정하기</button>
                                  </td>
                                  <td>
                                      <button class="sale_push_delete" data-item-id="${item.id}">삭제하기</button>
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


      // 모달 열기 이벤트
      $('#sale-items').on('click', '.sale__list__optionbtn', function() {
        const itemId = $(this).data('item-id');

        // 모달 내용을 초기화
        // 모달을 열기 전에 각 필드를 빈 값으로 초기화하여 이전에 조회된 데이터가 남아있지 않도록 합니다.
        $('#buyerEmail').text('');
        $('#itemName').text('');
        $('#itemPrice').text('');

        $.ajax({
          url: `/api/sellers/orders/${itemId}`,
          type: 'GET',
          headers: {
            'Authorization': accessToken
          },
          success: function(response) {
            showModal(response);
            $('#orderDetailModal').modal('show'); // 모달 열기
          },
          error: function(err) {
            console.error('Failed to load item details:', err);
          }
        });
      });


     // showModal 함수를 사용하여 모달의 내용을 업데이트
      function showModal(data) {
        $('#buyerEmail').text(data.buyerEmail);
        $('#itemName').text(data.nameKor);
        $('#itemPrice').text(data.price);
        $('#orderDetailModal').modal('show'); // 모달 열기
      }

     // 모달 닫기 이벤트
      $('.close').on('click', function() {
        $('#orderDetailModal').modal('hide');
      });


      // 수정 버튼 클릭 이벤트
      $('#sale-items').on('click', '.sale_push_edit', function() {
        const itemId = $(this).data('item-id');
        localStorage.setItem('selectedItemId', itemId);

        window.location.href = '/view/update/item';
      });

      // 삭제 버튼 클릭 이벤트
      $('#sale-items').on('click', '.sale_push_delete', function() {
        const itemId = $(this).data('item-id');

        if (confirm('정말로 이 상품을 삭제하시겠습니까?')) {
            $.ajax({
                url: `/api/items/${itemId}`,
                type: 'DELETE',
                headers: {
                    'Authorization': accessToken
                },
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

      $('#logo-link').on('click', function(event) {
          event.preventDefault();
          currentSearchType = '';
          currentKeyword = '';
          const searchUrl = '/view/home';
          window.location.href = searchUrl;
      });
  });

