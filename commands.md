### Format checks 
`sbt 'project rootJS' '++ 3' headerCheckAll scalafmtCheckAll 'project /' scalafmtSbtCheck`
`sbt "all scalafmtCheckAll"`
### ScalaFix check
`sbt 'project rootJS' '++ 3' 'scalafixAll --check'`
`sbt "all scalafixAll --check"`
### Format code
`sbt scalafmtAll`
