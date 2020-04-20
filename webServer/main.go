package main

import (
"github.com/gin-gonic/gin"
"os"
)

func main() {
  arg := os.Args[1]

  r := gin.Default()

  r.GET("/ping", func(c *gin.Context) {
    n :=1
    for i := 1; i <= 100000; i++ {
        for j :=1; j <= 5000; j++ {
            n += j
        }
    }
    c.JSON(200, gin.H{
      "message": "pong",
    })
  })

  r.Run(":"+arg)
}
