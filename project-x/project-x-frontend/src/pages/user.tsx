import { PlusCircleOutlined } from "@ant-design/icons";
import { Button, Table } from "antd";
import { useEffect, useState } from "react";
import CreateUserModal from "../components/modal/create.user.modal";
import { getUsersAPI } from "../services/api";

interface IUser {
  id: number;
  name: string;
  email: string;
}

const UserPage = () => {
  const [users, setUsers] = useState<IUser[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const fetchUsers = async () => {
    const res = await getUsersAPI();
    if (res?.data?.status === "success") {
      setUsers(res?.data?.data);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const columns = [
    {
      title: "ID",
      dataIndex: "id",
    },
    {
      title: "Name",
      dataIndex: "name",
    },
    {
      title: "Email",
      dataIndex: "email",
    },
  ];

  return (
    <div style={{ padding: 10 }}>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <h3>Table Users</h3>
        <Button
          onClick={() => setIsModalOpen(true)}
          type="primary"
          icon={<PlusCircleOutlined />}
        >
          ADD
        </Button>
      </div>
      <Table bordered dataSource={users} columns={columns} rowKey={"id"} />
      <CreateUserModal
        isModalOpen={isModalOpen}
        setIsModalOpen={setIsModalOpen}
        fetchUsers={fetchUsers}
      />
    </div>
  );
};

export default UserPage;
