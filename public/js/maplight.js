$(function() {
  var queryTypes = {
    company: {
      selector: ".refine-company",
      url: "/api/company"
    },
    individual: {
      selector: ".refine-individual",
      url: "/api/individual"
    },
    candidate: {
      selector: ".refine-candidate",
      url: "/api/candidate"
    },
    geographic: {
      selector: ".refine-geographic",
      url: "/api/geographic"
    }
  };

  $("input[name=query-type]").change(function() {
    var $el = $(this);

    $(".refine").children().hide();

    if ($el.is(":checked")) {
      $(queryTypeRefine[$el.val()]).show();
    }
  });

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

  var candidateNames = [
    "George Bush",
    "John Kerry",
    "Al Gore",
    "Bill Clinton"
  ];
  // Autocomplete multiple, courtesy of http://jqueryui.com/autocomplete/#multiple
  enableMultiAutocomplete($("[autocomplete-type=candidate]"), candidateNames);

  enableMultiAutocomplete($("[autocomplete-type=state]"), window.maplight.states);

  $(".run-query").click(function() {
    $(".run-query-error").hide();

    var queryType = $("input[name=query-type]:checked").val();
    if (!queryType) {
      $(".run-query-error").text("Please select a query type.");
      $(".run-query-error").show();
      return;
    }


  });
});
