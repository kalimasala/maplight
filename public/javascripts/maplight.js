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
  var enableMultiAutocomplete = function(el, values) {
    var split = function( val ) {
      return val.split( /,\s*/ );
    };
    console.log(values);
    el
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
        	console.log("request, response =", request.term, response);
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
    	var candidate_ac = $("[autocomplete-type=candidate]");
    	console.log('candidate_ac = ', candidate_ac);
      enableMultiAutocomplete(candidate_ac, candidateNames);
    });

  // $.widget( "custom.catcomplete", $.ui.autocomplete, {
  //   _renderMenu: function( ul, items ) {
  //     var that = this,
  //       currentCategory = "";
  //     $.each( items, function( index, item ) {
  //       if ( item.category != currentCategory ) {
  //         ul.append( "<li class='ui-autocomplete-category'>" + item.category + "</li>" );
  //         currentCategory = item.category;
  //       }
  //       that._renderItemData( ul, item );
  //     });
  //   }
  // });
  $("[autocomplete-type=location]").autocomplete({
      delay: 0,
      source: window.maplight.states
  });

  $(".run-query").unbind("click");
  $(".run-query").click(function() {
    $(".run-query-error").hide();

    var showError = function(msg) {
      $(".run-query-error").text(msg);
      $('.query-results').hide();
      $('.query-loading').hide();
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
      return toData && $(toData).val() || "";
    };

    var requestData = {
      "donor": $("#filter-donor").val(),
      "recipient": readData("filter-recipient"),
      "location-from": $("#filter-location-from").val(),
      "location-to": $("#filter-location-to").val(),
      "date-start": $("#filter-date-start").val(),
      "date-end": $("#filter-date-end").val()
    };

    console.log(requestData);
    $('.query-results').hide();
    $('.query-loading').show();
    $.post(
      "/api/donor",
      requestData
    ).done(function(resp) {
      $(".query-results").html(resp);
      $('.query-loading').hide();
      $('.query-results').show();
      $("#data-table").dataTable({
        bPaginate: false,
        oLanguage: {
          sSearch: "Filter: "
        }
      });
    }).fail(function() {
      showError("Error while fetching data!");
    });
    return false;
  });
});
