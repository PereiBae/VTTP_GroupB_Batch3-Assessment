export interface Tag{
  tag:string,
  count:number
}

export interface News{
  id: string,
  postDate: number
  postDateAsDate: Date
  title: string
  description: string
  image: string
  tags: Tag[]
}
