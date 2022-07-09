program Test;
var
  a: integer;
  b: integer = 50;

begin
   a := 10;
   repeat
      writeln('value of a: ', a);
      a := a + 1
   until a = 20;

   repeat
     writeln ('b =', b);
     b := b + 2;
   until b > 100;
end.