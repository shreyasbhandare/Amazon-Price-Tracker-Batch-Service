# Amazon Price Tracker Batch Service

This is a standalone batch service that runs at specific times in a day, fetches all the subscribed products from database, 
scrapes Amazon.com for latest prices and sends email to users. The technologies used are Core Java, Cassandra, Freemarker 
(email templating), Junit, Jsoup, Jakarta Mail etc.

## Usage

Navigate to [Amazon Price Tracker WebApp](https://amzn-price-tracker-io-service.herokuapp.com/) for subscribing to a product 
and check emails daily.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)