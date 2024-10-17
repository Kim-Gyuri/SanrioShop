/*!
* Start Bootstrap - Shop Homepage v5.0.6 (https://startbootstrap.com/template/shop-homepage)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-shop-homepage/blob/master/LICENSE)
*/
// This file is intentionally blank
// Use this file to add JavaScript to your project


    document.addEventListener('DOMContentLoaded', function() {
        document.querySelectorAll('.dropdown-submenu .dropdown-toggle').forEach(function(element) {
            element.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                var submenu = this.nextElementSibling;
                if (submenu.classList.contains('show')) {
                    submenu.classList.remove('show');
                } else {
                    document.querySelectorAll('.dropdown-submenu .dropdown-menu').forEach(function(el) {
                        el.classList.remove('show');
                    });
                    submenu.classList.add('show');
                }
            });
        });
    });
