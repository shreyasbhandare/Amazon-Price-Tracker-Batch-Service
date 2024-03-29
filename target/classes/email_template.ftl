<!DOCTYPE html>
<html>
    <body style="font-family: Arial, Helvetica, sans-serif; font-weight: bold">
        <div id="main-content">
            <#list products as product>
                <div class="product-container" style="white-space:nowrap; display: flex; align-items: center;  height: auto;">
                    <div class="product-img" style="display:inline; margin: 20px;">
                        <img src="${product.image}" style="height: 50px; width: 50px;" />
                    </div>
                    <div class="title-text" style="display:inline; white-space:nowrap; margin: auto 20px;">
                        <a href="${product.url}" style="text-decoration: none; color: black;">${product.name}</a>
                    </div>
                    <div class="price-text" style="display:inline; white-space:nowrap; margin: auto 20px; color: green;">
                        ${product.price}
                    </div>
                </div>
            </#list>
        </div>

        <div id="unsubscribe-content" style="margin: 20px">
            <a href="https://amzn-price-tracker-io-service.herokuapp.com/unsubscribe?email=${email}" style="color: red; font-size: 0.7em;">Unsubscribe</a>
        </div>
    </body>
</html>