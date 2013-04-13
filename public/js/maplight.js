$(function() {
  // var queryTypes = {
  //   donor: {
  //     selector: ".refine-donor",
  //     url: "/api/donor"
  //   },
  //   geographic: {
  //     selector: ".refine-geographic",
  //     url: "/api/geographic"
  //   }
  // };

  // $("input[name=query-type]").change(function() {
  //   var $el = $(this);

  //   $(".refine").children().hide();

  //   if ($el.is(":checked")) {
  //     $(queryTypes[$el.val()].selector).show();
  //   }
  // });

  // Autocomplete multiple, courtesy of http://jqueryui.com/autocomplete/#multiple
  var enableMultiAutocomplete = function($el, values) {
    var split = function( val ) {
      return val.split( /,\s*/ );
    };
    $el
      // don't navigate away from the field on tab when selecting an item
      .bind( "keydown", function( event ) {
        if ( event.keyCode === $.ui.keyCode.TAB &&
            $( this ).data( "ui-autocomplete" ).menu.active ) {
          event.preventDefault();
        }
      })
      .autocomplete({
        minLength: 0,
        source: function( request, response ) {
          var extractLast = function( term ) {
            return split( term ).pop();
          };
          // delegate back to autocomplete, but extract the last term
          response( $.ui.autocomplete.filter(
            values, extractLast( request.term ) ) );
        },
        focus: function() {
          // prevent value inserted on focus
          return false;
        },
        select: function( event, ui ) {
          var terms = split( this.value );
          // remove the current input
          terms.pop();
          // add the selected item
          terms.push( ui.item.value );
          // add placeholder to get the comma-and-space at the end
          terms.push( "" );
          this.value = terms.join( ", " );
          return false;
        }
      });
  };

  $.get("/api/autocomplete/candidates")
    .done(function(candidateNames) {
      enableMultiAutocomplete($("[autocomplete-type=candidate]"), candidateNames);
    });

  $.widget( "custom.catcomplete", $.ui.autocomplete, {
    _renderMenu: function( ul, items ) {
      var that = this,
        currentCategory = "";
      $.each( items, function( index, item ) {
        if ( item.category != currentCategory ) {
          ul.append( "<li class='ui-autocomplete-category'>" + item.category + "</li>" );
          currentCategory = item.category;
        }
        that._renderItemData( ul, item );
      });
    }
  });
  $("[autocomplete-type=location]").catcomplete({
      delay: 0,
      source: window.maplight.locations
  });

  $(".run-query").click(function() {
    $(".run-query-error").hide();

    var showError = function(msg) {
      $(".run-query-error").text(msg);
      $(".run-query-error").show();
    };

    // var queryType = $("input[name=query-type]:checked").val();
    // if (!queryType) {
    //   showError("Please select a query type.");
    //   return;
    // }

    // var queryApis = {
    //   donor: {
    //     data: function() {
    //       var toData = $("input[name=refine-donor-to]:checked").attr("data-selector");
    //       if (!toData) {
    //         showError("Please select To:");
    //         return;
    //       }
    //       return {
    //         from: $(".refine-donor-from").val(),
    //         to: $(toData).val()
    //       };
    //     }
    //   },
    //   geographic: {
    //     data: function() {
    //       return {
    //         from: $("#refine-geographic-from").val(),
    //         to: $("#refine-geographic-to").val()
    //       };
    //     }
    //   }
    // };

    var readData = function(radioGroupName) {
      var toData = $("input[name=" + radioGroupName + "]:checked").attr("data-selector");
      return toData && $(toData).val();
    };

    var requestData = {
      "donor-from": $(".refine-donor-from").val(),
      "donor-to": readData("refine-donor-to"),
      "location-from": $("#refine-geographic-from").val(),
      "location-to": $("#refine-geographic-to").val(),
      "year": $("#filter-year").val()
    };

    console.log(requestData);
    $.post(
      "/api/donor",
      requestData
    ).done(function(resp) {
      $(".data-table").html(resp);
      $("data-table").dataTable();
    }).fail(function() {
      showError("Error while fetching data!");
    });
  });
});
