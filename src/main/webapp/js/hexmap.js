/*
  jQuery routines for dealing with the hexmap
  
  TODO: really need to normalize row / column notation.  Currently 
  it incorrectly uses row as the y coordinate
*/

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
        var row = $(this).data("row");
        var column = $(this).data("column");
        $.tooltip("Hex position (row " + row + ", column " + column + ")");
        //highlightHex(row, column);
      });
    }
  }
}

/*
  Get a new map from the server
  XXX rename this
*/
function getNewMap(event) {
  // Get a new map via Ajax
  $.getJSON("/map/new.json", function(data) {
      populate(data["map"]);
  });
}

/*
  Get a path (really a list of points) from the server and highlight it.
*/
function getServerRandomPath() {
  // Get a new map via Ajax
  $.getJSON("/map/random-path.json", function(data) {
      selectPath(data["path"]).highlightHex("red");
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
  Set up event handling
*/
$(function() {
  $(".base", "#hexmap").live("click", function() {
    $(this).highlightHex();
  });
});

/*
  jQuery extension to add a tooltip to the screen
*/
(function($) {
  $.tooltip = function(message) {
    $('#tooltip').text(message);
  }
})(jQuery);

/*
  jQuery extension to place a tile based on row and column data 
  attached to the element set
  
  TODO should probably revisit this API
*/
(function($) {
  $.fn.hexMapPosition = function(row, column, additionalStyle) {

    // We store the row and column in the tile for 
    // use in the tooltip stuff
    this.data({"row": row, "column": column});
    
    // If the tile is class "base", we'll insert row and col as attributes
    if (this.hasClass("base")) {
      this.attr("data-row", row).attr("data-column", column);
    }
    
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
  Function to overlay a highlight on a particular hex
  
  TODO: should toggle highlight on or off
  BUG: when a highlight is set, it currently eats clicks so the overlayed tile doesn't get them
*/
(function($) {
  $.fn.highlightHex = function(color) {
    color = (color == null) ? "yellow" : color;
    
    $(this).each(function() {
      var row = $(this).data("row");
      var column = $(this).data("column");

      var tile = $(".highlight-" + color, "#templates").children().clone();

      tile.hexMapPosition(row, column).appendTo($('#hexmap'));
    });
    return this;
  }
})(jQuery);

/* 
  Custom selector to find base tiles in the grid at (x,y)
*/
(function($) {
  $.expr[":"].grid = function(element, index, meta) {
    if (! $(element).hasClass("base")) {
      return false;
    }
    
    // Validate input
    var params = meta[3].split(",");
    if (params.length != 2) {
      console.error(":grid selector requires two arguments, got", params);
      return false;
    }
    var requestedRow = Number(params[0]);
    if (isNaN(requestedRow)) {
      console.error(":grid selector row parameter requires a number, got", params[0]);
      return false;
    }
    var requestedColumn = Number(params[1]);
    if (isNaN(requestedColumn)) {
      console.error(":grid selector column parameter requires a number, got", params[1]);
      return false;
    }
    
    //console.debug("r:", requestedRow, "c:", requestedColumn);
    return ($(element).attr("data-row") == requestedRow) && 
           ($(element).attr("data-column") == requestedColumn) ;
  }
})(jQuery);


/*
  Given a list of (x,y) coordinates, return it as an element set
  
  This implementation is ridiculous.  There's got to be a better way but
  I can't for the life of me figure out how to get $().add() to work, so 
  instead I'm constructing a big unwieldy constructor on the fly
*/
function selectPath(path) {
  var selectors = [];
  for (var i = 0; i < path.length; i++) {
    var point = path[i];
    if (point.length != 2) {
      console.info("Skipping over malformed point", point, "in selectPath()");
      continue;
    }
    var column = point[0];
    var row = point[1];
    
    selectors.push("[data-row=" + row + "][data-column=" + column + "]");
  }
  
  var finalSelector = selectors.join(", ");
  //console.debug("final:", finalSelector);
  return $("#hexmap").find(finalSelector);
}

function testPath() {
  var path = [[0,1], [1,1], [2,2], [1,2]];
  selectPath(path).highlightHex();
}

/*
  Return the base hex tile at the given coordinates
*/
function selectHex(row, column) {
  return $(":grid(" + column + "," + row + ")", "#hexmap");
}