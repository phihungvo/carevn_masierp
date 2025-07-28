import { Input } from "antd"
import { ComponentProps, forwardRef, useEffect, useState } from "react"

type Props = ComponentProps<typeof Input> & {
  delay?: number
  regrex?: RegExp
  onCompletedChange?: (value: string) => void
  onChangeContent?: (value: string) => void
  convertValue?: (value: string) => string
}

const InputDelay = (props: Props) => {
  const {
    delay = 500,
    regrex,
    onCompletedChange,
    onChangeContent,
    convertValue,
    ...rest
  } = props;

  const [content, setContent] = useState<string>()

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let content = e.target.value
    if (regrex) {
      content = content.replace(regrex, '')
    }
    if (convertValue) {
      content = convertValue(content)
    }
    setContent(content)
    onChangeContent && onChangeContent(content)
  }

  useEffect(() => {
    let timer = undefined
    if (content !== undefined) {
      timer = setTimeout(() => {
        onCompletedChange?.(content)
      }, delay)
    }
    return () => clearTimeout(timer)
  }, [content])

  return (
    <Input
      {...rest}
      value={content || rest?.value}
      onChange={onChange}
    />
  );
}

export default forwardRef(InputDelay)
