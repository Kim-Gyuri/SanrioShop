$(document).ready(function() {
    function handleCategoryClick(event) {

        const mainCategory = $(this).data('category');
        const subCategory = $(this).data('subcategory');
        let accessToken = localStorage.getItem('accessToken');
        accessToken = accessToken ? accessToken.trim() : '';

        console.log('main={}', mainCategory);
        console.log('sub={}', subCategory);

        if (mainCategory) {
            let url = `/view/category?mainCategory=${encodeURIComponent(mainCategory)}`;
            if (subCategory) {
                url += `&subCategory=${encodeURIComponent(subCategory)}`;
            }

            window.location.href = url;
        }
    }

    // Attach click event handler to dropdown items
    $('.dropdown-item').click(handleCategoryClick);
});
