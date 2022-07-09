program Test;

procedure DoSomething (Para : char);
begin
  Writeln ('Got parameter : ', Para);
end;

begin
  DoSomething('Hello, World!')
end.