import { NavLink } from "react-router";
import { BarChart3 } from "lucide-react";

interface Props {
    size?: number;
    className?: string;
}

function Logo({ size = 28, className }: Props) {
    return (
        <NavLink to="/" className={className}>
            <BarChart3 size={size} className="text-sky-400" />
        </NavLink>
    );
}

export default Logo;
