var QUOTE_API  = "http://api.forismatic.com/api/1.0/";
var CHANGE_DUR =  800.0;
var MAX_YIQ    =  128;
var TWITTER    = "http://twitter.com/home/";

var IS_TOUCH =  !!("ontouchstart" in window)
                || window.navigator.msMaxTouchPoints > 0;

function zero_pad(s, len)
{

  if(s.length >= len) {
    return s;
  }

  var pad = new Array(1 + len).join('0');

  return (pad + s).slice(-len);
}

function contrast_yiq(color_code)
{
  var r = (color_code >> 16) & 0xFF;
  var g = (color_code >> 8 ) & 0xFF;
  var b = (color_code >> 0 ) & 0xFF;

  return ((r*299)+(g*587)+(b*114))/1000;
}

function random_color()
{
  var rnd;

  do {
    rnd = Math.random() * Math.pow(2, 24);
  } while(contrast_yiq(rnd) > MAX_YIQ);

  var code = Math.floor(rnd);
  var hex  = code.toString(16);

  return '#' + zero_pad(hex, 6);
}

function tweet_link(quote)
{
  return TWITTER + '?' + $.param({
     'status': quote
  });
}

function set_quote(quote, attribution, link)
{

  if(attribution === '') {
    attribution = 'an unkown author';
  }

  $('#quote').text(quote);
  $('#quote-block').attr('cite', link);
  $('#attribution').text(attribution);
  $('#attribution').attr('href', link);
  $('#twitter').attr('href', tweet_link(quote));
}

function set_quote_json(json)
{
  var text = JSON.stringify(json);
  set_quote(json.quoteText, json.quoteAuthor, json.quoteLink);
}

function fetch_quote()
{
  var params = {
    method: 'getQuote',
    key: '457653',
    format: 'jsonp',
    lang: 'en'
  };
  return $.ajax(
    {
      dataType: 'jsonp',
      method: 'GET',
      url: QUOTE_API,
      data: params,
      jsonp: 'jsonp',
      contentType: "application/jsonp; charset=utf-8",
      timeout: 3000
    }
  );
}

function fade_out_text()
{
  return $('.animated-text').animate(
    {
      'opacity': 0.0
    },
    {
      'duration': CHANGE_DUR / 2,
      'queue': false
    }
  );
}

function fade_in_text()
{
  return $('.animated-text').animate(
    {
      'opacity': 1.0
    },
    {
      'duration': CHANGE_DUR / 2,
      'queue': false
    }
  );
}

function change_colors(color)
{
  $(
    '.animated-button, .animated-box'
  ).animate(
    {
      'background-color': color,
      'border-color': color
    },
    {
      'duration': CHANGE_DUR,
      'queue': false
    }
  );
  $('.animated-text').animate(
    {
      'color': color
    },
    {
      'duration': CHANGE_DUR,
      'queue': false
    }
  );
}

function un_hover(selector)
{
  if(!IS_TOUCH) {
    return;
  }
  $(selector).each(function(i){
    console.log(i);
    var el   = $(this).get(0);
    var par  = el.parentNode;
    var sib  = el.nextSibling;

    par.removeChild(el);
    setTimeout(function() {par.insertBefore(el, sib);}, 0);
  });
}

function update_quote()
{
  var rnd_color = random_color();
  change_colors(rnd_color);

  $.when(fetch_quote(), fade_out_text()).then(
    function(a1, a2) {
      set_quote_json(a1[0]);
      fade_in_text();

      un_hover('.animated-button');
    }
    , function(a1, a2) {
      fade_in_text();
      set_quote('Quote Server Error. Try Again.', 'The Creator.');
      un_hover('.animated-button');
    }
  );
}

$(document).ready(function()
{

  $('body').addClass('animated-box');

  update_quote();

  $('#get-quote').on('click', update_quote);
});