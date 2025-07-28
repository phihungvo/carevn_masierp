export type MapKey<Arr = any, Obj = any> = {
  data?: {
    arr?: Arr,
    obj?: Obj,
    totalRecord?: number,
  }
}

export type BasicSelect<T = string> = {
  label: string,
  value: T,
}

export type MapKeySelect<Ext = any> = MapKey<BasicSelect & Ext>
