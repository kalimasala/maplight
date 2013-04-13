$(function() {
  var queryTypeRefine = {
    company: ".refine-company",
    individual: ".refine-individual",
    candidate: ".refine-candidate",
    geographic: ".refine-geographic"
  };

  $("input[name=query-type]").change(function() {
    var $el = $(this);

    $(".refine").children().hide();

    if ($el.is(":checked")) {
      $(queryTypeRefine[$el.val()]).show();
    }
  });

  var candidateNames = [
    "George Bush",
    "John Kerry",
    "Al Gore",
    "Bill Clinton"
  ];
  $(".autocomplete-candidate").autocomplete({
    source: candidateNames
  });
});
