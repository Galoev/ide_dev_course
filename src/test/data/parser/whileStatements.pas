program Test;
var
   a : integer;
   x : real = 1024;

begin
   a := 10;
   while  a < 20  do
   begin
      writeln('value of a: ', a);
      a := a + 1;
   end;

   while x >= 2 do
     x :=  x / 2;
end.