<!DOCTYPE html>
<html>
    <head>
        <style>
          @import url('https://fonts.googleapis.com/css2?family=BioRhyme:wght@700&family=Sora:wght@500;600&display=swap');
          body {
             font-family: 'BioRhyme', serif;
             font-family: 'Sora', sans-serif;
          }

          #main-content {
          }

          a {
            text-decoration: none;
            color: black;
          }

          .product-container {
            display: flex;
            align-items: center;
          }

          .product-img {
            margin: 20px;
          }

          .product-img img {
            height: 50px;
            width: 50px;
          }

          .title-text {
            margin-left: 20px;
          }

          .price-text {
            margin-left: 20px;
            color: green;
          }
        </style>
    </head>

    <body>
        <div id="main-content">
            <#list products as product>
                <div class="product-container" style="white-space:nowrap">
                    <div class="product-img" style="display:inline;">
                        <img src="${product.image}" />
                    </div>
                    <div class="title-text" style="display:inline; white-space:nowrap;">
                        <a href="${product.url}">${product.name}</a>
                    </div>
                    <div class="price-text" style="display:inline; white-space:nowrap;">
                        ${product.price}
                    </div>
                </div>
            </#list>
        </div>
    </body>
</html>