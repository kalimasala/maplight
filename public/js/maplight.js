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
      $(queryTypes[$el.val()].selector).show();
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

    var showError = function(msg) {
      $(".run-query-error").text(msg);
      $(".run-query-error").show();
    };

    var queryType = $("input[name=query-type]:checked").val();
    if (!queryType) {
      showError("Please select a query type.");
      return;
    }

    var queryApis = {
      company: {
        data: function() {
          var toData = $("input[name=refine-company-to]:checked").attr("data-selector");
          if (!toData) {
            showError("Please select To:");
            return;
          }
          return {
            from: $(".refine-company-from").val(),
            to: $(toData).val()
          };
        }
      },
      individual: {
        data: function() {
          var toData = $("input[name=refine-individual-to]:checked").attr("data-selector");
          if (!toData) {
            showError("Please select To:");
            return;
          }
          return {
            from: $(".refine-individual-from").val(),
            to: $(toData).val()
          };
        }
      },
      candidate: {
        data: function() {
          var fromData = $("input[name=refine-candidate-from]:checked").attr("data-selector");
          if (!fromData) {
            showError("Please select From:");
            return;
          }
          return {
            to: $(".refine-candidate-to").val(),
            from: $(fromData).val()
          };
        }
      },
      geographic: {
        data: function() {
          var data = $("input[name=refine-geographic]:checked").attr("data-selector");
          if (!data) {
            showError("Please select From: or To:");
            return;
          }
          return {
            data: $(data).val()
          };
        }
      }
    };

    console.log(queryApis[queryType].data());
  });
});
