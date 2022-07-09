program Test;

var
  Number : integer = 12345;
  C : char = 'a';
  i : integer = 5;

begin
  case Number of
   1..10   : WriteLn ('Small number');
   11..100 : WriteLn ('Normal, medium number');
  else
   WriteLn ('HUGE number');
  end;

  case C of
   'a' : WriteLn ('A pressed');
   'b' : WriteLn ('B pressed');
   'c' : WriteLn ('C pressed');
  else
    WriteLn ('unknown letter pressed : ', C);
  end;

  case C of
   'a','e','i','o','u' : WriteLn ('vowel pressed');
   'y' : WriteLn ('This one depends on the language');
  else
    WriteLn ('Consonant pressed');
  end;

  case i of
   3 : DoSomething;
   1..5 : DoSomethingElse;
  end;

end.