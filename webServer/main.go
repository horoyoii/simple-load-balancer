package main

import (
"github.com/gin-gonic/gin"
"os"
"fmt"

)

func main() {
  arg := os.Args[1]

  r := gin.Default()

  r.GET("/auth", func(c *gin.Context) {
   fmt.Println("=-------------------------")

   fmt.Println(c.Request.Header.Get("KONG"))
   fmt.Println(c.Request.Header.Get("Connection"))
   //time.Sleep(100 * time.Second)
   c.JSON(200, gin.H{
      "message": "goood",
    })
  })

  r.GET("/ping", func(c *gin.Context) {
   fmt.Println(c)
    
   c.JSON(200, gin.H{
      "ok": "gooood",
    })
  })
  r.Run(":"+arg)
}
