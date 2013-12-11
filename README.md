# Jalfrezi

A tool which scrapes a set of share prices and calculates a number of moving average trends over time.

Shares are stored in a directory, in the directory is a file for each share named <symbol>.json which contains:
* name: The name of the share
* symbol: The share symbol of the share
* prices: an associative array key'd by date (yyyymmdd) whose value is the price information for the last 100 days, where the price information is:
  * price: The closing price (depends on when the extraction takes place)
  * volume: The volume of shares sold that day
  * low: The lowest price of the day
  * high: The highest price of the day
  * change: The % change compared to the previous day (not sure about this)
  * 30DayPrice: The 30 day moving average price
  * 50DayPrice: The 50 day moving average price

Each time the tool is run it will:
* Record the run date, this will form the price information key.
* Scrape the source for all the share price information.
* For each share price:
  * Read the <symbol>.json file and add the new price info.
  * If there is more than 100 entries discard the oldest price info.
  * If there is sufficient entries calculate the share moving averages.
