import React, { useState } from 'react'

type ActionType = {
  accept: boolean,
  reject: boolean,
  request_accept: boolean,
  cancel: boolean,
  id?: string,
  key?: 'accept' | 'reject' | 'request_accept' | 'cancel'
}

const useAction = () => {
  const [actions, setActions] = useState<ActionType>({
    accept: false,
    reject: false,
    request_accept: false,
    cancel: false,
  })


  const handleToggleAction = (payload: { key: ActionType['key'], id?: string }) => () => {
    setActions(pre => ({
      ...pre,
      [payload.key]: !pre[payload?.key],
      id: payload?.id || actions?.id
    }))
  }

  return {
    actions,
    handleToggleAction
  }
}

export default useAction
