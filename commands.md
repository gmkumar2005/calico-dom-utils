### Format checks 
`sbt 'project rootJS' '++ 3' headerCheckAll scalafmtCheckAll 'project /' scalafmtSbtCheck`
`sbt "all scalafmtCheckAll"`
### ScalaFix check
`sbt 'project rootJS' '++ 3' 'scalafixAll --check'`
`sbt "all scalafixAll --check"`
### Format code
`sbt scalafmtAll`
### ScalaFix and format code
`all scalafmtAll; all scalafixAll`
### Check all
`all scalafixAll --check ; all scalafmtCheckAll ; all headerCheckAll`
### Gpg
`echo test | gpg -e -r kiran@akkagrpc.com | gpg -d`
`echo test | gpg -e -r F7E440260BAE93EB4AD2723D6613CA76E011F638 | gpg -d`
`echo test | gpg -e | gpg -d`
`echo "message" | gpg --clearsign --local-user F7E440260BAE93EB4AD2723D6613CA76E011F638`
`gpg-agent --daemon `
`gpg-agent --server`

`GPG_TTY=$(tty)
export GPG_TTY`

`echo 'it works' | gpg --clearsign`