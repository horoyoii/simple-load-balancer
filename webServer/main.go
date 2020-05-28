package main

import (
"github.com/gin-gonic/gin"
"os"
"fmt"
)

func main() {
  arg := os.Args[1]

  r := gin.Default()

  r.GET("/ping", func(c *gin.Context) {
   fmt.Println(c)
   c.JSON(200, gin.H{
      "message": "pong",
    })
  })

  r.Run(":"+arg)
}
