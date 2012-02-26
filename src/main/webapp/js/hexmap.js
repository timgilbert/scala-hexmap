/* 
  Return a list of lists of strings. The top-level list 
  represents columns, and each column list is a series of 
  strings where the string value corresponds to the name
  of a tile in the templates div.
*/
function getHexMap(numColumns, numRows) {
  var columns = [];
  for (var c = 0; c < numColumns; c++) {
    var row = [];
    for (var r = 0; r < numRows; r++) {
      row.push(randomTile());
    }
    columns.push(row);
  }
  return columns;
}

// For testing - return a random tile
function randomTile() {
  // There's probably a way to get this out of the DOM via jQuery
  var choices = ["blue", "brown", "darkgreen", "grey", "white", "lightgreen"];
  var num = Math.floor(Math.random() * choices.length);
  return choices[num];
}

/*
  Given a 2d array like that returned from getHexMap(),
  iterate through it and position the named tiles in 
  their proper places in the hexmap div
*/
function populate(hexmap) {
  // Clear the old map, if any
  $('#hexmap').children().remove();
  for (var x = 0; x < hexmap.length; x++) {
    var column = hexmap[x];
    for (var y = 0; y < column.length; y++) {

      // clone the tile and place it
      var tile = placeTile(column[y], x, y);

      // Add some event handling
      tile.bind("mouseover", function(event) {
        //$(this).highlightHex();
        $.tooltip("Hex position (" + $(this).data("row") + ", " + $(this).data("column") + ")");
      });
    }
  }
}

function getNewMap(event) {
  // Get a new map via Ajax
  $.getJSON("/map/new.json", function(data) {
      populate(data["map"]);
  });
}

/*
  Given a tile name and row/column numbers, make a clone 
  of the tile and place it in the hexmap.
*/
function placeTile(name, x, y) {
  var tile = $("." + name, "#templates").children().clone();

  tile.hexMapPosition(x, y).appendTo($('#hexmap'));
  return tile;
}

/*
  jQuery extension to place a tile based on row and column data 
  attached to the element set
*/
(function($) {
  $.tooltip = function(message) {
    $('#tooltip').text(message);
  }
})(jQuery);

/*
  jQuery extension to place a tile based on row and column data 
  attached to the element set
*/
(function($) {
  $.fn.hexMapPosition = function(row, column, additionalStyle) {

    // We store the row and column in the tile for 
    // use in the tooltip stuff
    this.data({"row": row, "column": column});

    var tile_width = this.attr("width");
    var tile_height = this.attr("height");

    // Haven't done the math to check these but they work
    var y_offset = tile_height / 2;
    var x_offset = tile_width / 4;

    var xpos = 0 + (row * (tile_width - x_offset));
    var ypos = 0 + (column * tile_height);

    // Am I really the only developer on earth who
    // hates CSS positioning?
    ypos += 40; 

    if (row % 2 == 1) {
      ypos += y_offset;
    }

    //console.debug("Placing hex(r " + row + ", c" + column + ") at (" + xpos + ","  + ypos + ")");
  
    var style = {"position": "absolute", 
                 "top": ypos, "left": xpos};
    $.extend(style, additionalStyle);   // nb, not sure this is really needed vs css classes
    
    return this.css(style);
  }
})(jQuery);

/*
  Draw a highlight on the given hex
*/
function highlightHex(row, column) {
  var tile = $(".highlight", "#templates").children().clone();
  
  //console.debug("Highlighting " + row + "," + column);
  tile.hexMapPosition(row, column).appendTo($('#hexmap'));
}

// hmm
(function($) {
  $.fn.highlightHexXXX = function(row, column) {
    
    var row = $(this).data("row");
    var column = $(this).data("column");
    
    var tile = $(".highlight", "#templates").children().clone();
  
    var style = {"z-index": 100};
    
    tile.hexMapPosition(row, column, style).appendTo($('#hexmap'));
    return this;
  }
})(jQuery);
