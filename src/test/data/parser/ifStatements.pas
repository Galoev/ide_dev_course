program Test;
var
  a : integer;
  b : integer = 123;

begin
  a := 100;
  if (a < 20 ) then
    writeln('a is less than 20' )
  else
    writeln('a is not less than 20' );

  if (b = 123) then
    begin
    if (a < 20 ) then
      writeln('a is less than 20' )
    else
      writeln('a is not less than 20' );
    end;
  else
    writeln('Hello World' );

end.