program Test;
var
  a: integer;
  b: integer;

begin
  for a := 10 to 20 do
  begin
    writeln('value of a: ', a);
  end;

  for b := 20 downto 10 do
    begin
      writeln('value of a: ', b);
    end;
end.