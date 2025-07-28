import { useEffect, useState } from 'react';

export const useToggleExpandableRow = <T extends object>(data: T) => {
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);

  const openClass = (id: string) => {
    const childrenList = document.querySelectorAll('.child-row');
    const parentList = document.querySelectorAll('.parent-row');

    childrenList.forEach(child => {
      if (child.classList.contains(`child-row-${id}`)) {
        child.classList.remove('hidden');
      }
    });

    parentList.forEach(parent => {
      if (parent.classList.contains(`parent-row-${id}`)) {
        parent.classList.add('selected');
      }
    });
  };

  const closeClass = (id: string) => {
    const childrenList = document.querySelectorAll('.child-row');
    const parentList = document.querySelectorAll('.parent-row');

    childrenList.forEach(child => {
      if (child.classList.contains(`child-row-${id}`)) {
        child.classList.add('hidden');
      }
    });

    parentList.forEach(parent => {
      if (parent.classList.contains(`parent-row-${id}`)) {
        parent.classList.remove('selected');
      }
    });
  };

  const toggleRowKeys = (id: string) => {
    if (selectedRowKeys.includes(id)) {
      closeClass(id);
      setSelectedRowKeys(prevState => prevState.filter(key => key !== id));
    } else {
      setSelectedRowKeys(prevState => [...prevState, id]);
    }
  };

  useEffect(() => {
    selectedRowKeys.forEach(id => {
      openClass(id);
    });
  }, [selectedRowKeys, data]);

  return { selectedRowKeys, toggleRowKeys, setSelectedRowKeys };
};
